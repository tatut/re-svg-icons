(ns icons
  "Generate icons"
  (:require [clojure.xml :as xml]
            [clojure.java.io :as io]
            [clojure.string :as str]))


(defn svg->hiccup
  [transform-svg-root {:keys [tag] :as node}]
  (let [{:keys [attrs content]} (if (= tag :svg)
                                  (transform-svg-root node)
                                  node)
        hiccup [tag]
        hiccup (if (seq attrs)
                 (conj hiccup (dissoc attrs :xmlns))
                 hiccup)]
    (into hiccup (map (partial svg->hiccup transform-svg-root) content))))

(comment
  ;; not needed at the moment, the repo contains proper svg files as well
  (defn remove-tabler-tags [file]
    (let [out (java.io.File/createTempFile "tabler-cleaned" ".svg")]
      (.deleteOnExit out)
      (->> file io/reader line-seq
           (drop 1) ;; drop first "---" line
           (drop-while #(not= % "---")) ;; lines between the "---" lines
           (drop 1) ;; drop the last "---" line
           (str/join "\n")
           (spit out))
      out)))

(defn icon-file->hiccup
  ([file] (icon-file->hiccup {} file))
  ([svg-xf file]
   {:name (first (str/split (.getName file) #"\."))
    :hiccup (->> file xml/parse (svg->hiccup svg-xf))}))

(defn convert-icons [icon-path source-file ns-name svg-xf]
  (spit (io/file source-file)
        (str "(ns " ns-name "\n  (:require [re-svg-icons.core :refer [icon*]]))\n\n"
             (str/join "\n\n"
                       (for [file (.listFiles (io/file icon-path))
                             :let [{:keys [name hiccup]} (icon-file->hiccup svg-xf file)]]
                         (str "(defn " name
                              " ([] (" name " {}))"
                              " ([opts] (icon* opts " (pr-str hiccup) ")))"))))))
(defn feather-icons []
  (convert-icons "icon-sources/feather/icons"
                 "src/re_svg_icons/feather_icons.cljs"
                 "re-svg-icons.feather-icons"
                 identity))

(defn tabler-icons []
  (convert-icons "icon-sources/tabler-icons/icons"
                 "src/re_svg_icons/tabler_icons.cljs"
                 "re-svg-icons.tabler-icons"
                 identity))

(defn open-iconic-icons []
  (convert-icons "icon-sources/open-iconic/svg"
                 "src/re_svg_icons/open_iconic.cljs"
                 "re-svg-icons.open-iconic"

                 ;; Normalize open iconic to same size and set colors
                 (fn [{:keys [tag attrs content] :as node}]
                   {:tag tag
                    :attrs (merge attrs
                                  {:width 24
                                   :height 24
                                   :viewBox "0 0 24 24"
                                   :stroke "none"
                                   :fill "currentColor"})
                    ;; add SVG group around content that scales from 8x8 => 24x24
                    :content [{:tag :g
                               :attrs {:transform "scale(3)"}
                               :content content}]})))


(defn -main [& _args]
  (feather-icons)
  (tabler-icons)
  (open-iconic-icons))
