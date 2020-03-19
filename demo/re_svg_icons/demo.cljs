(ns re-svg-icons.demo
  (:require [reagent.core :as r]
            [re-svg-icons.feather-icons :as fi]
            [re-svg-icons.tabler-icons :as ti]
            [re-svg-icons.open-iconic :as oi]))

(def some-feather-icons [["wind" fi/wind]
                         ["bar-chart-2" fi/bar-chart-2]
                         ["download" fi/download]
                         ["list" fi/list]])

(def some-tabler-icons [["sort-descending" ti/sort-descending]
                        ["mood-happy" ti/mood-happy]
                        ["ticket" ti/ticket]
                        ["puzzle" ti/puzzle]])

(def some-open-iconic-icons [["lock-locked" oi/lock-locked]
                             ["calculator" oi/calculator]
                             ["browser" oi/browser]
                             ["thumb-up" oi/thumb-up]])
(defn icons [icons opts]
  [:<>
   [:div {:style {:display :flex :flex-direction :row
                  :width "500px"
                  :justify-content :space-between}}
    (doall
     (for [[name icon] icons]
       ^{:key name}
       [:div [icon opts] name]))]
   [:hr]])

(defn icon-demo [title demo-icons]
  [:div
   [:h3 title]
   ;; regular icon
   [:div
    "Regular icons"
    [icons demo-icons {}]]

   ;; icons with specified properties
   [:div
    "Icons with options map (width&height)"
    [icons demo-icons {:width 42 :height 42}]]

   ;; Icons with a class
   [:div
    "Icons with class"
    [icons demo-icons {:class "red-rotated"}]]])

(defn demo-component []
  [:<>
   [icon-demo "FEATHER ICONS" some-feather-icons]
   [icon-demo "TABLER ICONS" some-tabler-icons]
   [icon-demo "OPEN ICONIC" some-open-iconic-icons]])

(defn ^:export main []
  ;; remove figwheel generated app element (we don't want to provide index.html)
  (js/document.body.removeChild (js/document.querySelector "#app"))
  (let [style (js/document.createElement "style")

        app (js/document.createElement "div")]
    (set! (.-innerText style) ".red-rotated { color: red; transform: rotate(45deg); ")
    (js/document.head.append style)
    (js/document.body.append app)
    (r/render [demo-component] app)))

(main)
