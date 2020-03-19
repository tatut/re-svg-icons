(ns icons
  "Generate icons"
  (:require [clojure.xml :as xml]
            [clojure.java.io :as io]
            [clojure.string :as str]))

(defn svg->hiccup [{:keys [tag attrs content]}]
  (let [hiccup [tag]
        hiccup (if (seq attrs)
                 (conj hiccup (dissoc attrs :xmlns))
                 hiccup)]
    (into hiccup (map svg->hiccup content))))

(defn icon-file->hiccup [file]
  {:name (first (str/split (.getName file) #"\."))
   :hiccup (-> file xml/parse svg->hiccup)})

(defn feather-icons []
  (let [path (io/file "icon-sources/feather/icons")]
    (spit (io/file "src/re_svg_icons/feather_icons.cljs")
          (str "(ns re-svg-icons.feather-icons\n  (:require [re-svg-icons.core :refer [icon*]]))\n\n"
               (str/join "\n\n"
                         (for [file (.listFiles path)
                               :let [{:keys [name hiccup]} (icon-file->hiccup file)]]
                           (str "(defn " name
                                " ([] (" name " {}))"
                                " ([opts] (icon* opts " (pr-str hiccup) ")))")))))))

(defn -main [& _args]
  (feather-icons))
