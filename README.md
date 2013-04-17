# Clojure-offline - разрешение зависимостей проекта вручную.

Приложение задеплоено здесь: https://clojureoffline-kostafey.rhcloud.com

Пример строки артефактов, для которых ищутся зависимости:
[[org.clojure/clojure "1.5.1"] [compojure "1.1.5"] [me.raynes/laser "1.1.1"]]

## Running

Запускать приложение так:

    lein ring server

War-ку собирать так:
    
    lein ring uberwar
    
## License

Copyright © 2013 FIXME
