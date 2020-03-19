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
  ([file] (icon-file->hiccup identity file))
  ([pre-parse-fn file]
   {:name (first (str/split (.getName file) #"\."))
    :hiccup (-> file pre-parse-fn xml/parse svg->hiccup)}))

(defn convert-icons [icon-path source-file ns-name pre-parse-fn]
  (spit (io/file source-file)
        (str "(ns " ns-name "\n  (:require [re-svg-icons.core :refer [icon*]]))\n\n"
             (str/join "\n\n"
                       (for [file (.listFiles (io/file icon-path))
                             :let [{:keys [name hiccup]} (icon-file->hiccup pre-parse-fn file)]]
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

(defn -main [& _args]
  (feather-icons)
  (tabler-icons))
