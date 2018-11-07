package de.codecentric.androidtestktx.demo

import org.junit.runner.RunWith
import org.junit.runners.Suite

const val INPUT_TEXT = "Type text"
const val REPLACE_TEXT = "Replace text"

@RunWith(Suite::class)
@Suite.SuiteClasses(
    KTXEspressoDemo::class,
    KTXUIAutomatorDemo::class
)
class AllTests