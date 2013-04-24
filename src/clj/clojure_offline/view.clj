(ns clojure-offline.view
  (:use clojure-offline.lein-caller)
  (:require [me.raynes.laser :as l]
            [clojure.java.io :refer [file]]))

(def is-debug true)

(defn get-html-intern [url]
  (l/parse
   (slurp (clojure.java.io/resource url))))

(def get-html (if is-debug
                get-html-intern
                (memoize get-html-intern)))

(defn get-main-html []
  (get-html "public/html/main.html"))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; Pages

(defn show-artifacts-tree [artifact]
  (l/document (get-main-html)
              (l/id= "atrifacts-script")
              (l/content (l/unescaped (clj-off-get-script artifact)))))

(defn show-clojure-offline []
  (l/document (get-main-html)))
