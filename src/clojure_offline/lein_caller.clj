(ns clojure-offline.lein-caller
  (:require
   [clojure.java.io :as io]
   [cemerick.pomegranate.aether :as aether])
  (:use [clojure.repl :only [doc]]
        clojure.pprint
        leiningen.core.classpath 
        leiningen.core.main
        leiningen.core.project))

;; default-repositories

(defn clj-off-get-script [artifacts-list]
  (let [dep (load-string (str "'" artifacts-list))]
    ;; (let [dep artifacts-list]
    (str (dependency-hierarchy 
          :dependencies
          {:dependencies dep}))))



