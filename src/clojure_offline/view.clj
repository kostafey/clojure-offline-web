(ns clojure-offline.view
  (:use clojure-offline.lein-caller)
  (:require [me.raynes.laser :as l]
            [clojure.java.io :refer [file]]))


(def main-html
  (l/parse
   (slurp (clojure.java.io/resource "public/html/main.html"))))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; Pages

(defn show-artifacts-tree [artifact]
  (l/document main-html
              (l/id= "atrifacts-script")
              ;; (l/content
              ;;  (artifact-script-frag artifact))
              (l/content (clj-off-get-script artifact))))

(defn show-clojure-offline []
  (l/document main-html))
