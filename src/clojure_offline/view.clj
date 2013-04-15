(ns clojure-offline.view
  (:require [me.raynes.laser :as l]
            [clojure.java.io :refer [file]]))


(def main-html
  (l/parse
   (slurp (clojure.java.io/resource "public/html/main.html"))))


;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; Fragments

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; Pages


(defn show-clojure-offline []
  (l/document main-html))
