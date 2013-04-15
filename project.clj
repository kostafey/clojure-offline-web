(defproject task4a "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.5.1"]
                 [compojure "1.1.5"]
                 [me.raynes/laser "1.1.1"]
                 [mysql/mysql-connector-java "5.1.24"]
                 [korma "0.3.0-RC5"]
                 [lib-noir "0.4.9"]
                 [leiningen-core "2.1.3"]]
  :plugins [[lein-ring "0.8.2"]]
  :ring {:handler clojure-offline.handler/app}
  :profiles
  {:dev {:dependencies [[ring-mock "0.1.3"]]}})
