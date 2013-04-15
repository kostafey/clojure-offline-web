(ns task4a.lein-caller
  (:require
   [clojure.java.io :as io]
   [cemerick.pomegranate.aether :as aether])
  (:use [clojure.repl :only [doc]]
        clojure.pprint
        leiningen.core.classpath 
        leiningen.core.main
        leiningen.core.project))

(in-ns 'task4a.lein-caller)
(load "/leiningen/core/classpath")

;; default-repositories

(pprint
 (aether/dependency-hierarchy
  '[[incanter "1.2.3"]]
  (aether/resolve-dependencies 
   :coordinates '[[incanter "1.2.3"]]
   :repositories (merge cemerick.pomegranate.aether/maven-central 
                        {"clojars" "http://clojars.org/repo"}))))

(pprint 
 (aether/resolve-dependencies 
  :coordinates '[[leiningen-core "2.1.3"]]
  :repositories (merge cemerick.pomegranate.aether/maven-central 
                       {"clojars" "http://clojars.org/repo"})))

(doc aether/resolve-dependencies)
(doc aether/resolve-dependencies*)
(doc aether/dependency-hierarchy)

(dependency-hierarchy 
 :dependencies
 {:dependencies '[[leiningen-core "2.1.3"]]})


(resolve-dependencies :dependencies {:dependencies '[[leiningen-core "2.1.3"]]})

(get-dependencies-urls :dependencies {:dependencies '[[leiningen-core "2.1.3"]]})

(defn get-dependencies-urls
  [dependencies-key {:keys [repositories native-path] :as project} & rest]
  (do
    (in-ns 'leiningen.core.classpath)
    (let [dependencies-tree
          (apply get-dependencies dependencies-key project rest)
          jars (->> dependencies-tree
                    (aether/dependency-files)
                    (filter #(re-find #"\.(jar|zip)$" (.getName %))))
          native-prefixes (get-native-prefixes (get project dependencies-key)
                                               dependencies-tree)]
      (when-not (= :plugins dependencies-key)
        (or (when-stale :extract-native [dependencies-key] project
                        extract-native-deps jars native-path native-prefixes)
            ;; Always extract native deps from SNAPSHOT jars.
            (extract-native-deps (filter #(re-find #"SNAPSHOT" (.getName %)) jars)
                                 native-path
                                 native-prefixes)))
      jars)))



(make
 {:dependencies [['leiningen-core "2.1.3"]]
  }
 'myblog 
 "0.1.0-SNAPSHOT"
 (.getParent (io/file *file*))
 )

