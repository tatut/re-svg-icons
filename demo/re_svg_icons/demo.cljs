(ns re-svg-icons.demo
  (:require [reagent.core :as r]
            [re-svg-icons.feather-icons :as fi]))

(def some-icons [["wind" fi/wind]
                 ["bar-chart-2" fi/bar-chart-2]
                 ["download" fi/download]
                 ["list" fi/list]])

(defn demo-component []
  [:div
   "FEATHER ICONS"
   ;; regular icon
   [:div
    "Regular icons"
    [:div {:style {:display :flex :flex-direction :row
                   :width "300px"
                   :justify-content :space-between}}
     (doall
      (for [[name icon] some-icons]
        ^{:key name}
        [:div [icon] name]))]]
   [:hr]

   ;; icons with specified properties
   [:div
    "Icons with options map (width&height)"
    [:div {:style {:display :flex :flex-direction :row
                   :width "300px"
                   :justify-content :space-between}}
     (doall
      (for [[name icon] some-icons]
        ^{:key name}
        [:div [icon {:width 66 :height 66}] name]))]]
   [:hr]

   ;; Icons with a class
   [:div
    "Icons with class"
    [:div {:style {:display :flex :flex-direction :row
                   :width "300px"
                   :justify-content :space-between}}
     (doall
      (for [[name icon] some-icons]
        ^{:key name}
        [:div [icon {:class "red-rotated"}] name]))]]])

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
