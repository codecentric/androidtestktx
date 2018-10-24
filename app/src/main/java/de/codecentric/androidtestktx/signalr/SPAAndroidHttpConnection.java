package com.daimler.mbpro.android.core.signalr;

import android.net.http.AndroidHttpClient;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.SocketTimeoutException;
import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import microsoft.aspnet.signalr.client.LogLevel;
import microsoft.aspnet.signalr.client.Logger;
import microsoft.aspnet.signalr.client.Platform;
import microsoft.aspnet.signalr.client.http.HttpConnection;
import microsoft.aspnet.signalr.client.http.HttpConnectionFuture;
import microsoft.aspnet.signalr.client.http.Request;
import microsoft.aspnet.signalr.client.http.StreamResponse;
import org.apache.http.Header;
import org.apache.http.HttpHost;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.entity.StringEntity;
import org.apache.http.message.BasicHttpEntityEnclosingRequest;

/**
 * This class implements a creation of HttpConnection for SignalR, without any usage of AsyncTasks.
 * Request running in the background is implemented with {@link BackgroundExecutor} thread pool
 * and {@link RequestExecutor}
 */
public class SPAAndroidHttpConnection implements HttpConnection {

  private final BackgroundExecutor backgroundExecutor;
  private Logger mLogger;

  public SPAAndroidHttpConnection(Logger logger) {
    if (logger == null) {
      throw new IllegalArgumentException("logger");
    } else {
      this.mLogger = logger;
    }

    backgroundExecutor = new BackgroundExecutor();
  }

  public HttpConnectionFuture execute(
      final Request request,
      final HttpConnectionFuture.ResponseCallback responseCallback
  ) {

    this.mLogger.log("Create new RequestExecutor for HTTP Connection", LogLevel.Verbose);
    final HttpConnectionFuture future = new HttpConnectionFuture();

    final RequestExecutor requestExecutor = new RequestExecutor(backgroundExecutor) {
      AndroidHttpClient mClient;
      InputStream mResponseStream;

      @Override void onBackgroundWork() {
        if (request == null) {
          future.triggerError(new IllegalArgumentException("request"));
        } else {
          this.mClient = AndroidHttpClient.newInstance(Platform.getUserAgent());
          this.mResponseStream = null;

          try {
            SPAAndroidHttpConnection.this.mLogger.log(
                "Create an Android-specific request",
                LogLevel.Verbose
            );
            request.log(SPAAndroidHttpConnection.this.mLogger);
            HttpRequest realRequest = SPAAndroidHttpConnection.createRealRequest(request);
            URI uri = new URI(request.getUrl());
            HttpHost host = new HttpHost(uri.getHost(), uri.getPort(), uri.getScheme());
            SPAAndroidHttpConnection.this.mLogger.log("Execute the HTTP Request", LogLevel.Verbose);

            HttpResponse response = this.mClient.execute(host, realRequest);
            SPAAndroidHttpConnection.this.mLogger.log("Request executed", LogLevel.Verbose);
            this.mResponseStream = response.getEntity().getContent();

            Header[] headers = response.getAllHeaders();
            Map<String, List<String>> headersMap = new HashMap<>();

            for (Header header : headers) {
              String headerName = header.getName();
              if (headersMap.containsKey(headerName)) {
                headersMap.get(headerName).add(header.getValue());
              } else {
                List<String> headerValues = new ArrayList<>();
                headerValues.add(header.getValue());
                headersMap.put(headerName, headerValues);
              }
            }

            responseCallback.onResponse(
                new StreamResponse(this.mResponseStream, response.getStatusLine().getStatusCode(),
                                   headersMap
                ));
            future.setResult(null);
          } catch (SocketTimeoutException timeoutException) {
            SPAAndroidHttpConnection.this.mLogger.log(
                "Timeout executing request: " + timeoutException.getMessage(), LogLevel.Information);
            future.triggerTimeout(timeoutException);
          } catch (Exception exception) {
            SPAAndroidHttpConnection.this.mLogger.log(
                "Error executing request: " + exception.getMessage(),
                LogLevel.Critical
            );
            future.triggerError(exception);
          } finally {
            this.closeStreamAndClient();
          }
        }
      }

      @Override void closeStreamAndClient() {
        if (this.mResponseStream != null) {
          try {
            this.mResponseStream.close();
          } catch (IOException ignored) {

          }
        }

        if (this.mClient != null) {
          this.mClient.close();
        }
      }
    };

    future.onCancelled(new Runnable() {
      public void run() {
        requestExecutor.closeStreamAndClient();
      }
    });

    requestExecutor.execute();

    return future;
  }

  private static BasicHttpEntityEnclosingRequest createRealRequest(Request request)
      throws UnsupportedEncodingException {
    BasicHttpEntityEnclosingRequest realRequest =
        new BasicHttpEntityEnclosingRequest(request.getVerb(), request.getUrl());
    if (request.getContent() != null) {
      realRequest.setEntity(new StringEntity(request.getContent()));
    }

    Map<String, String> headers = request.getHeaders();
    for (String key : headers.keySet()) {
      realRequest.addHeader(key, headers.get(key));
    }

    return realRequest;
  }

  /**
   * Class which mimics the AsyncTask behavior by creating Runnable and waiting {@link RequestExecutor#execute()}
   * method to be called
   */
  abstract static class RequestExecutor {

    private final BackgroundExecutor backgroundExecutor;
    private final Runnable runnable;

    RequestExecutor(BackgroundExecutor backgroundExecutor) {
      this.backgroundExecutor = backgroundExecutor;

      runnable = new Runnable() {
        @Override public void run() {
          onBackgroundWork();
        }
      };
    }

    /**
     * In order to run a background job, this method must be called manually.
     */
    void execute() {
      backgroundExecutor.execute(runnable);
    }

    /**
     * All background work should be places inside implementation of this method.
     */
    abstract void onBackgroundWork();

    abstract void closeStreamAndClient();
  }
}