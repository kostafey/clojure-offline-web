(ns clojure-offline.lein-caller
  (:require
   [clojure.java.io :as io]
   [cemerick.pomegranate.aether :as aether])
  (:use [clojure.repl :only [doc]]
        clojure.pprint
        leiningen.core.classpath 
        leiningen.core.main
        leiningen.core.project)
  (:import java.io.StringWriter))

;; default-repositories

(defn to-p-string [m] 
  (let [w (StringWriter.)] 
    (pprint m w)
    (clojure.string/replace (.toString w) #"\n" "<br>") ))

(defn get-hierarchy [artifacts-list] 
    (dependency-hierarchy 
          :dependencies
          {:dependencies artifacts-list}))

(defn clj-off-get-script [artifacts-list]
  (let [dep (load-string (str "'" artifacts-list))]
    (str "<h2>Dependences list</h2>"
         (to-p-string (get-hierarchy dep)))))

(defn art-wanderer [art]
  (if (coll? art)
    (if (symbol? (nth art 0))
      (str art)
      (art-wanderer art))   
    ))

;; (art-wanderer (get-hierarchy '[[leiningen-core "2.1.3"]]))
;; (to-p-string (get-hierarchy '[[leiningen-core "2.1.3"]]))



