package com.daimler.mbpro.android.core.uiautomator

import android.app.Activity
import android.content.Intent
import com.daimler.mbpro.android.core.uiautomator.extensions.appStarts
import com.daimler.mbpro.android.core.uiautomator.extensions.device
import java.util.UUID
import kotlin.reflect.KClass

abstract class UiAutomatorRobot<T : Activity>(val kClass: KClass<T>, autoStartActivity: Boolean) {

  @Inject
  lateinit var bearerRepository: BearerRepository
  @Inject
  lateinit var customerRepository: CustomerRepository
  @Inject
  lateinit var vehicleRepository: VehicleRepository
  @Inject
  lateinit var userRepository: UserRepository

  protected val injector: TestAppComponent by lazy { (targetContext.applicationContext as TestApp).testAppComponent }
  private lateinit var bearer: Bearer

  abstract fun inject()

  init {
    if (autoStartActivity) {
      startActivity()
    }
  }

  fun startActivity() {
    improveDeviceStartupConditions()

    val intent = Intent(targetContext, kClass.java)
    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
    targetContext.startActivity(intent)
    device waitUntil appStarts()
  }

  private fun improveDeviceStartupConditions() {
    closeDialogWithButton("Cancel")
    closeDialogWithButton("Ok")
    closeDialogWithButton("Continue")
    closeDialogWithButton("Force close")
  }

  private fun closeDialogWithButton(buttonText: String) {
    val button = viewByText { buttonText }
    if (button.exists()) {
      button.click()
    }
  }

  protected fun onAuthorizedApi(request: Bearer.() -> Single<User>): User {
    return bearerRepository.getBearerToken()
      .doOnSuccess { bearer = it }
      .flatMap { request(it) }
      .blockingGet()
  }

  fun createCustomer(customerDependentFns: Customer.() -> Single<User>): Single<User> {
    return customerRepository.create(bearer, Customer(UUID.randomUUID().toString()))
      .flatMap { customer -> customerDependentFns(customer) }
  }

  infix fun Single<Any>.andThen(driverSingle: Single<User>): Single<User> {
    return flatMap { driverSingle }
  }

  @Suppress("UseExpressionBody")
  private fun Customer.vehicle(index: Int): VehicleProfile {
    return VehicleProfile("WDS0000100P$customerId$index", registrationPlate = "FE-T $index")
  }

  fun Customer.createVehicles(): Single<Any> = createVehicles(3)

  fun Customer.createVehicles(vehiclesCount: Int): Single<Any> {
    return createVehicles {
      (1..vehiclesCount)
        .map { vehicle(it) }
        .toList()
    }
  }

  private fun Customer.createVehicles(vehiclesFn: () -> List<VehicleProfile>): Single<Any> {
    return Observable.fromIterable(vehiclesFn())
      .flatMapSingle { vehicleRepository.createVehicle(bearer, it, CustomerProfile(customerId, guid)) }
      .toList()
      .map { Any() }
  }

  fun Customer.createDriver(): Single<User> {
    return userRepository.createUser(
      bearer,
      User(userKey = UUID.randomUUID().toString(), customerIds = arrayOf(customerId))
    )
  }
}