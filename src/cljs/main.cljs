(ns clojure-offline.main
  (:require [enfocus.core :as ef]
            [goog.dom :as dom])
  (:require-macros [enfocus.macros :as em]))

(declare home 
         home-page 
         gstarted-page
         doc-resources
         doc-resources-page)

(em/deftemplate searcher "html/searcher.html" [])

(defn scroll-to []
  (ef/chainable-standard
    (fn [nod]
      (. nod (scrollIntoView)))))

(defn reset-scroll []
  (ef/chainable-standard
    (fn [nod]
      (set! (.-scrollTop nod) 0))))

(em/defaction setup-pane [width height]
  ["#home-button"] (em/listen :click home-page)
  ["#gstarted-button"] (em/listen :click #(gstarted-page width height))
  ["#doc-resources"] (em/listen :click #(doc-resources-page width height))
  )

(defn init-content-pane []
    (let [size (dom/getViewportSize)
          width (- (.-width size) 40)
          height (- (.-height size) 70)]
      (setup-pane (- width 10)  (- height 10))
      ;; (setup-pane width height)
      ))

;; (em/deftemplate home "main.html" [])

(em/defaction home-page []
  ["#searcher"] (em/content (searcher))
  ["#caption"] (em/content "Clojure offline")
  ["#content-pane"] (em/chain
                     (em/content "")
                     (em/set-style :padding "0px")
                     (em/resize 0 0 500))
  ["#searcher"] (em/fade-in 500 nil)
  ["#caption"] (em/fade-in 500 nil))

(defn is-home []
  (= (em/from (em/select ["#caption"]) 
              (em/get-text)) ""))

(em/deftemplate gstarted "/doc-help" [])

(em/defaction gstarted-page [width height]  
  ["#content-pane"] (em/chain
                     (reset-scroll)
                     (em/resize width height (if (is-home) 0 500))
                     (em/set-style :padding "10px")
                     (em/content (gstarted)))
  ["#searcher"] (em/chain
                 (em/fade-out 500 nil)
                 (em/content ""))
  ["#caption"] (em/chain
                (em/fade-out 500 nil)
                (em/content "")))

(em/deftemplate doc-resources "/doc-resources" [])

(em/defaction doc-resources-page [width height]  
  ["#content-pane"] (em/chain                    
                     (reset-scroll)
                     (em/resize width height (if (is-home) 0 500))
                     (em/set-style :padding "10px")
                     (em/content (doc-resources)))
  ["#searcher"] (em/chain
                 (em/fade-out 500 nil)
                 (em/content ""))
  ["#caption"] (em/chain
                (em/fade-out 500 nil)
                (em/content "")))

(em/defaction start []
  ;; (em/at js/document
  ;;   ["#menu"] (em/content init-content-pane) ;(em/content "Hello world3!")
  ;;   )
  ;; ["body"] (em/do-> (init-content-pane))
  ;; ["body"] (em/chain setup-pane)
  [".marea"] (em/listen 
              :mouseenter
              #(em/at (.-currentTarget %)
                      ["h3"] (em/do-> (em/add-class "blur-highlight"))))
  [".marea"] (em/listen 
              :mouseleave
              #(em/at (.-currentTarget %)
                      ["h3"] (em/remove-class "blur-highlight")))
  ["#doc-menu"] (em/listen 
              :mouseenter
              #(em/at (.-currentTarget %)
                      [".sub"] (em/resize :curwidth 85 500)))
  ["#doc-menu"] (em/listen 
              :mouseleave
              #(em/at (.-currentTarget %)
                      [".sub"] (em/resize :curwidth 0 500)
                      ["h3"] (em/remove-class "blur-highlight")))
  ["#src-menu"] (em/listen 
              :mouseenter
              #(em/at (.-currentTarget %)
                      [".sub"] (em/resize :curwidth 60 500)))
  ["#src-menu"] (em/listen 
              :mouseleave
              #(em/at (.-currentTarget %)
                      [".sub"] (em/resize :curwidth 0 500)))
  )

(set! (.-onload js/window) ;start
      #(do (init-content-pane)
           (start))
)
