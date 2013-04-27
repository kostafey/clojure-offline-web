(ns clojure-offline.main
  (:require [enfocus.core :as ef]
            [goog.dom :as dom])
  (:require-macros [enfocus.macros :as em]))

(declare home 
         home-page 
         gstarted-page
         doc-trans-page
         clone-for-demo
         get-attr-demo
         doc-templates-page
         doc-from-page)

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

  ;; ["#doc-trans"] (em/listen :click doc-trans-page)
  ;; ["#doc-events"] (em/listen :click doc-events-page) 
  ;; ["#doc-effects"] (em/listen :click doc-effects-page)
  ;; ["#doc-remote"] (em/listen :click doc-template-page)
  ;; ["#doc-extract"] (em/listen :click doc-from-page)  

  ;; ["#content-pane"] (em/chain
  ;;                    (em/resize 5 height 500)
  ;;                    (em/resize width :curheight 500))
  )

(defn init-content-pane []
    (let [size (dom/getViewportSize)
          width (- (.-width size) 40)
          height (- (.-height size) 70)]
      (setup-pane width height)))

;; (em/deftemplate home "main.html" [])

(em/defaction home-page []
  ;; [".iron-body"] (reset-scroll)
  ["#content-pane"] (em/chain
                     (em/content "")
                     (em/resize 0 0 500))
  ["#searcher"] (em/fade-in 500 nil)  
  ["#caption"] (em/fade-in 500 nil))

;; (em/deftemplate gstarted "getting-started.html" [])
(em/deftemplate gstarted "/help" [])

(em/defaction gstarted-page [width height]  
  ["#content-pane"] (em/chain
                     (em/content (gstarted))
                     (reset-scroll)
                     (em/resize 5 height 500)
                     (em/resize width :curheight 500))
  ["#searcher"] (em/fade-out 500 nil)
  ["#caption"] (em/fade-out 500 nil))

(em/defaction start []
  ;; (em/at js/document
  ;;   ["#menu"] (em/content init-content-pane) ;(em/content "Hello world3!")
  ;;   )
  ;; ["body"] (em/do-> (init-content-pane))
  ;; ["body"] (em/chain setup-pane)
  [".marea"] (em/listen 
              :mouseenter
              #(em/at (.-currentTarget %)
                      [".sub"] (em/resize :curwidth 145 500)
                      ["h3"] (em/do-> (em/add-class "blur-highlight"))))
  [".marea"] (em/listen 
              :mouseleave
              #(em/at (.-currentTarget %)
                      [".sub"] (em/resize :curwidth 0 500)
                      ["h3"] (em/remove-class "blur-highlight")))

  )

(set! (.-onload js/window) ;start
      #(do (init-content-pane)
           (start))
)
