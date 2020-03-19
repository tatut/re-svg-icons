(ns re-svg-icons.core)

(defn icon* [opts svg-hiccup]
  ;; Merge the given opts to the SVG attrs
  ;; This allows setting class and other options
  (update svg-hiccup 1 merge opts))
