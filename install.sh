# use graalvm configure
mvn clean install
native-image -cp target/Practice-1.0-SNAPSHOT.jar -H:Class=controller.PokemonDemo -H:Name=PokemonDemoNative