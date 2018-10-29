# Android Test KTX

[![Version](https://api.bintray.com/packages/bajicdusko/maven/androidtestktx/images/download.svg)](https://bintray.com/bajicdusko/maven/androidtestktx/_latestVersion)

This library is a collection of Kotlin extension and infix functions made to increase 
the readability and decrease the boilerplate of `Espresso` and `UIAutomator` matchers
and actions.

It is inteded to work well with **RobotPattern**, as the naming convention of the 
functions assume to have some sort of semantical context on the call site.

This is a **Work In Progress**, and contributions are highly welcomed. For the 
future plans of library development, please check the [Wiki](https://github.com/bajicdusko/androidtestktx/wiki). 

Simple test example:

```kotlin
@Test
fun shouldLoginUser_whenStartingTheAppForTheFirstTime() {
  withLoginRobot {
    loginDemoUser()
  } andThen {
    acceptThePermissions
  } andThenVerifyThat {
    userIsLoggedIn()
  }
}
```

### Show me the code

In order to login the user properly, we have to find a login button, and username and password fields.

**Unified Espresso/UIAutomator syntax - KTX**
```kotlin
class MainRobot {
    
    @Test
    fun shouldLoginDemoUser(){
        typeText("dummyUsername") into text(R.id.activityLoginEditTextUsername)
        typeText("dummyPassword") into text(R.id.activityLoginEditTextPassword)
        click on button(R.id.activityLoginBtnLogin)
        
        MainActivity::class verifyThat { itIsDisplayed() }
    }
}
```

In the old fashion way, we'd do something like this:

**Espresso syntax**
```kotlin
class MainRobot {
    
    @Test
    fun shouldLoginDemoUser(){
        onView(withId(R.id.activityLoginEditTextUsername)).perform(typeText("dummyUsername"))
        onView(withId(R.id.activityLoginEditTextPassword)).perform(typeText("dummyPassword"))
        onView(withId(R.id.activityLoginBtnLogin)).perform(click())
    }
}
```

**UIAutomator syntax**
```kotlin
class MainRobot {
    
    @Test
    fun shouldLoginDemoUser(){
        val instrumentation = InstrumentationRegistry.getInstrumentation()
        val uiDevice = UiDevice.getInstance(instrumentation)
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        
        val usernameSelector = UiSelector().resourceId(appContext.resources.getResourceName(
                R.id.activityLoginEditTextUsername
            )
        )
        val usernameTextField = uiDevice.findObject(usernameSelector)
        usernameTextField.text = "dummyUsername"
        val passwordSelector = UiSelector().resourceId(appContext.resources.getResourceName(
                R.id.activityLoginEditTextPassword
            )
        )
        val passwordTextField = uiDevice.findObject(passwordSelector)
        passwordTextField.text = "dummyPassword"
        val loginButtonSelector = UiSelector().resourceId(appContext.resources.getResourceName(
                R.id.activityLoginBtnLogin
            )
        )
        val loginButton = uiDevice.findObject(loginButtonSelector)
        loginButton.click()
    }
}
```

# Usage

In the `build.gradle` file of your app module, put this line into the dependencies:
```
androidTestImplementation 'de.codecentric:androidtestktx:0.9.0'
```

Make sure that in the project level `build.gradle` file, you're using the JCenter as repository source:

```
allprojects {
  repositories {
    ...
    jcenter()
  }
}
```

# LICENSE

[Apache 2.0](https://github.com/bajicdusko/androidtestktx/blob/master/LICENSE)
