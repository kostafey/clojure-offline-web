(ns clojure-offline.handler
  (:use compojure.core)
  (:require [compojure.handler :as handler]
            [compojure.route :as route]
            ;; [myblog.model :as model]
            [clojure-offline.view :as view]
            [ring.util.response :as resp]
            [noir.util.middleware :as noir]
            [noir.session :as session]))

(defn show-clojure-offline []
  (view/show-clojure-offline))

(defn find-artifacts [artifact]
  (view/show-artifacts-tree artifact)
  ;; (resp/redirect "/")
  )

(defroutes app-routes
  
  ;; (GET "/" [] (resp/redirect "/html/main.html") ;(show-clojure-offline)
  ;;      )
  
  (GET "/" []  (show-clojure-offline))                   
  (POST "/find-artifacts" [artifact] (find-artifacts artifact))
  
  (GET "/help" [] (view/get-doc-html))
  (route/resources "/") 
  (route/not-found "Not Found"))

(def app
  (->
   [(handler/site app-routes)]
   noir/app-handler
   noir/war-handler
   ))


(comment
  ;; Function for inspecting java objects
  (use 'clojure.pprint)
  (defn show-methods [obj]
    (-> obj .getClass .getMethods vec pprint)) 
  )


