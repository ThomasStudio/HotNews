# Consumer rules for the base library module
# These rules are applied when the library is consumed by other modules

# Keep public classes and their methods that might be used by consumers
-keep public class com.hotnews.base.** {
    public *;
}

# Don't obfuscate the base library classes
-dontobfuscate