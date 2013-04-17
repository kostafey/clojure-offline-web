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
  ;; (str artifact)
  
  (view/show-artifacts-tree artifact)
  ;; (resp/redirect "/")
  )

;; (defn update-article [id header content]
;;   (let [article {:id (pint id), :header header, :content content}]
;;     (model/update-article article)
;;     (view/show-article article)))

(defroutes app-routes
  
  (GET "/" [] (show-clojure-offline))
  
  (POST "/find-artifacts" [artifact] (find-artifacts artifact))

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


