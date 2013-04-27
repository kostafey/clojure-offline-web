(ns clojure-offline.view
  (:use clojure-offline.lein-caller
        markdown.core)
  (:require [me.raynes.laser :as l]
            [clojure.java.io :refer [file]]))

(def is-debug true)

(def get-file
  (let [get-file-intern
        (fn [url]
          (slurp (clojure.java.io/resource url)))]
    (if is-debug
      get-file-intern
      (memoize get-file-intern))))

(def get-html
  (let [get-html-intern
        (fn [url]
          (l/parse
           (slurp (clojure.java.io/resource url))))]
    (if is-debug
      get-html-intern
      (memoize get-html-intern))))

(defn get-main-html []
  (get-html "public/html/main.html"))

(defn get-doc-html []
  (md-to-html-string (get-file "public//html/help.md")
                     :code-style #(str "class=\"" % "\"")))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; Pages

(defn show-artifacts-tree [artifact]
  (l/document (get-main-html)
              (l/id= "atrifacts-script")
              (l/content (l/unescaped (clj-off-get-script artifact)))))

(defn show-clojure-offline []
  (l/document (get-main-html)))
