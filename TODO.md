Pre-release:
- Support multiple units of the same dimension (min/hr, ft/mile)
- Setting to prefer US or Metric units
- In-app purchase for pro mode
- Make input text size shrink with number of characters
- Ripple color for color schemes
- Bug where m/s is marsed as mHz
- Horizontal mode sizing/layout
- 5V input has wrong number of sigfigs when being converted into mV
- Equals resets caret to the end

- Weird error line for the input "()"

Post-release:
- Properly round when a number is shortened
- Unit corrections
- Small space between every three digits in the output (should be a setting)
- Report bug button
- Suggest feature button
- Animate secondary button appearing
- Better job of showing sigfigs in input
- Sigfigs for transindental numbers and log/exp/trig functions (https://en.wikipedia.org/wiki/Significance_arithmetic#Transcendental_functions)
- Refactor CalculatorButton into two subclasses and a superclass for the secondary and primary buttons
- Nonintegral powers
- Unparenthesized functions
- Secondary buttons swap with primary when they're used so they're easier to press next time
- Calculation History
- Undo button (undo in history?)
- Option: Always use scientific vs natural notation for the results
- Option to always round results to a certain decimal place or the number of sigfigs
- Support for affine units (temperature)
- Support for area units (like the acre)
- Support for logarithmic units
- Make the unit exponent too large error only highlight the number which is too large instead of the entire unit
- Abbreviation alias (for degree, abbreviated deg)
- Gi/Mi/Ki prefixes
- support for sin^-1()
- Refactor magnitude so that 3 is 0 and .1 is -1
- Support for imaginary numbers
- Support for rational unit exponents
- Persist preferred unit settings
- Short and long scale names for the unit prefixes
- Factor MainActivity into different parts (billing, input, display, ...)
- Support for different number formats in different locales
- Hex mode (data only?)
- Test figureStart in SigfigNumber.valueString
- Setting for units written with negative exponents vs. with the slash
- Use U+23E8 or e instead of E for scientific notation
  + Also have an option to always use a plus sign in the exponent
- Make the ErrorInput not show underlines when using swiftkey
- log(1e3) isn't quite equal to 3

Pro Mode:
- Unlock all skins