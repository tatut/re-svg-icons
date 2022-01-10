# re-svg-icons

Free SVG icons as simple hiccup generating ClojureScript functions.

Contains MIT licensed icons ready to use in ClojureScript as simple SVG function
component. Icons can also be modified by passing an optional map of options for
the toplevel svg element (like dimensions or class).

# Icon sources

The following MIT licensed icon sets are included in the library:

* [Feather icons](https://github.com/feathericons/feather)
* [Tabler Icons](https://github.com/tabler/tabler-icons)
* [Open Iconic](https://github.com/iconic/open-iconic)
* [Heroicons](https://github.com/refactoringui/heroicons)
* [Iconoir](https://github.com/lucaburgio/iconoir)

See [all icons here](https://tatut.github.io/re-svg-icons/all-icons.html).

# Demo and usage

## Basic usage

All icons are just functions that return SVG in hiccup format so
they can be used as a reagent component directly.

```clojure
[:div "download icon: " [feather-icons/download]]
```

All icons take optional options map.
See demo/re_svg_icons/demo.cljs for sample:

![](demo/demo.png?raw=true)
