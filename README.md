# Clojure-offline - manual resolve clojure dependences.

The application is deployed at: https://clojureoffline-kostafey.rhcloud.com

<!-- Run app: lein ring server -->
<!-- Build war: lein ring uberwar -->

# Quick Start

Enter the list of leiningen's dependences, like this and press `SEARCH`, e.g.:

```
[[org.clojure/clojure "1.5.1"] [compojure "1.1.5"] [me.raynes/laser "1.1.1"]]
```

If you nee a single artifact, it should be passesd as array too, e.g.:

```
[[leiningen-core "2.1.3"]]
```

The `Dependences list` - is a list of flattened dependency hierarchy of all
passed artifacts.

The `Downloads list` - is a list of probable locations of the all found
dependenses. Obviously, it should be run on the computer with internet.

The `Install list` is a script, that should be run in the target (probably
offline) computer from the folder, where all listed *.jar and *.pom files is
located. The %HOME% string should be replaced with the real home path.

## License

Copyright © 2013 Kostafey <kostafey@gmail.com>

Distributed under the General Public License 2.0+
