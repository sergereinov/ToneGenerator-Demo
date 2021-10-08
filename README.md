## ToneGenerator Demo

### Simplest test of generated tones
To play single beep
```kotlin
val generator = ToneGenerator(AudioManager.STREAM_NOTIFICATION, 100)
generator.startTone(ToneGenerator.TONE_CDMA_ALERT_CALL_GUARD, 200)
```

### Playing ToneGenerator's tones

![screenshot](/doc/screenshot.png)
