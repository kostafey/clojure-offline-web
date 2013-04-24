(ns clojure-offline.main
  (:require [enfocus.core :as ef])
  (:require-macros [enfocus.macros :as em]))

(defn start []
  (em/at js/document
    ["#menu"] (em/content "Hello world3!")))

(set! (.-onload js/window) start)
