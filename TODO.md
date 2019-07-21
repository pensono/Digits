Pre-release:
- Clear all when converting to a unit should hide the conversion ui

Post-release:
- Calculation History
- Button to control number of digits in preview
- Ripple color for color schemes
- European locale (commas and periods where they shouldn't be)
- Unit corrections
- Animate secondary button appearing
- Better job of showing sigfigs in input
- Sigfigs for transindental numbers and log/exp/trig functions (https://en.wikipedia.org/wiki/Significance_arithmetic#Transcendental_functions)
- Refactor CalculatorButton into two subclasses and a superclass for the secondary and primary buttons
- Nonintegral powers
- Unparenthesized functions
- Undo button (undo in history?)
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
- Tablet mode
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