(ns clojure-offline.lein-caller
  (:require
   [clojure.java.io :as io]
   [cemerick.pomegranate.aether :as aether])
  (:use [clojure.repl :only [doc]]
        [clojure.string :only (join split)]
        clojure.pprint
        leiningen.core.classpath 
        leiningen.core.main
        [leiningen.core.project :only [default-repositories]]
        )
  (:import java.io.StringWriter))

(defn to-p-string [m] 
  (let [w (StringWriter.)] 
    (pprint m w)
    (clojure.string/replace (.toString w) #"\n" "<br>") ))

(defn get-hierarchy [artifacts-list]
  (aether/dependency-hierarchy
   artifacts-list
   (aether/resolve-dependencies 
    :coordinates artifacts-list
    :repositories default-repositories)))

(def artifacts (atom (list)))

(defn art-wanderer [art]
  (if (and 
       (coll? art)
       (vector? art)
       (not (coll? (nth art 0))))
    (swap! artifacts conj art)
    (dorun (map art-wanderer art))))

(defn run-wanderer [art]  
  (reset! artifacts (list))
  (dorun (map art-wanderer art))
  @artifacts)

(defn get-dependeces-list [artifacts-list]
  (-> (str "'" artifacts-list) 
      load-string get-hierarchy run-wanderer))

(defn parse-artifact [artifact-name]
  "Parse `artifact-name' to list (`group-id' `artifact-id' `version')
Input format, e.g.:
 [org.clojure/clojure \"1.5.1\"]
Ouptut format, e.g.:
 (\"org.clojure\" \"clojure\" \"1.5.1\")"
  (let [group-and-artifact (split (str (first artifact-name)) #"/")
        group-id (first group-and-artifact)
        artifact-id (if (nil? (second group-and-artifact))
                      (first group-and-artifact)
                      (second group-and-artifact))
        version (second artifact-name)]
    (list group-id artifact-id version)))

(defmacro with-artifact [artifact-name & body]
  "Inject `group-id' `artifact-id' `version' local variables to the `body'
scope."
  `(let [artifact# (parse-artifact ~artifact-name)
         ~(symbol "group-id") (nth artifact# 0)
         ~(symbol "artifact-id") (nth artifact# 1)
         ~(symbol "version") (nth artifact# 2)]
     ~@body))

(defn get-jar-urls [artifact-name]
  "Convert from leiningen's `artifact-name' to probably jar url location on
clojars or maven central.
E.g. convert from
 [org.clojure/clojure \"1.5.1\"]
to
 https://clojars.org/repo/lein-ring/lein-ring/0.8.2/lein-ring-0.8.2.jar"
  (with-artifact
    artifact-name
    (let [art-path (str
                    (reduce #(str %1 "/" %2) (split group-id #"\.")) "/"
                    artifact-id "/" version "/"
                    artifact-id "-" version ".jar")]
      (reduce #(str %1 %2)
              (map #(str (-> % second :url) art-path "\n")
                   default-repositories)))))

(defn get-localrepo-install [artifact-name]
  "Convert from leiningen's `artifact-name' to local maven repository creation
script.
lein localrepo install <filename> <[groupId/]artifactId> <version>

E.g. convert from
 [org.clojure/clojure \"1.5.1\"]
to
 lein localrepo install foo-1.0.6.jar com.example/foo 1.0.6"
  (with-artifact
    artifact-name
    (str "lein localrepo install "
         artifact-id "-" version ".jar "
         group-id "/" artifact-id " " version)))

(defn clj-off-get-script [artifacts-list]
  (let [deps (get-dependeces-list artifacts-list)]
    (str "\n<h2 style=\"text-align: center;\">Dependences list</h2>\n"
         "<ul><li>"
         (reduce #(str %1 "<li>" %2 "\n") deps)
         "</ul>"
         "<p>"
         "\n<h2 style=\"text-align: center;\">Downloads list</h2>\n"
         "<div style=\"margin-left: 50px;\">"
         (loop [acc (StringBuffer. "")
                d deps]
           (if (empty? d) 
             acc
             (recur (.append acc
                             (join (map 
                                    #(str "wget " % "<br>\n") 
                                    (split (get-jar-urls (first d)) #"\n"))))
                    (rest d))))
         "</div>"
         "<p>"
         "\n<h2 style=\"text-align: center;\">Install list</h2>\n"
         "<div style=\"margin-left: 50px; margin-bottom: 80px;\">"
         (join (map #(str % "<br>\n") (map get-localrepo-install deps)))
         "</div>")))

(comment
  (str "wget "
       (reduce #(str %1 "\n" "wget " %2)
               (split (get-jar-urls '[org.clojure/clojure "1.5.1"]) #"\n"))
       "<br>")

  (reduce #(str %1 "/" %2) (split "org.clojure" #"\."))

  (clj-off-get-script '[[org.clojure/clojure "1.5.1"]])

  (clj-off-get-jar-urls '[org.clojure/clojure "1.5.1"])

  (clj-off-with-artifact '[org.clojure/clojure "1.5.1"] artifact-id)

  (macroexpand-1 '(clj-off-with-artifact '[org.clojure/clojure "1.5.1"] artifact-id))

  (clj-off-parse-artifact '[org.clojure/clojure "1.5.1"])

  (clj-off-get-script '[[compojure "1.1.5"]])

  (run-wanderer (get-hierarchy '[[leiningen-core "2.1.3"]]))
  
  (art-wanderer 
   (get-hierarchy '[[leiningen-core "2.1.3"]]))

  (to-p-string (get-hierarchy '[[leiningen-core "2.1.3"]]))
  )
