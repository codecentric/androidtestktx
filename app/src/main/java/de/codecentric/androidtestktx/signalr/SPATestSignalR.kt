package com.daimler.mbpro.android.core.signalr

class SPATestSignalR(url: String, installationId: String, tokenProvider: TokenProvider) :
  SignalR(url, installationId, tokenProvider) {
  override fun getHttpConnection(logger: Logger?): HttpConnection = SPAAndroidHttpConnection(logger)
}