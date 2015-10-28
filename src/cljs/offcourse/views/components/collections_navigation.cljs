(ns offcourse.views.components.collections-navigation
  (:require [offcourse.helpers.css :as css]
            [clojure.string :as string]
            [quiescent.dom :as d]))

(defn Collection-Button [collection-name {on-click :go-to-collection}]
  (d/button {:className "btn btn-inverse browse"
             :onClick #(on-click collection-name)}
            (string/capitalize (name collection-name))))

(defn Collections-Navigation [collection-names handlers]
  (d/nav {}
         (d/ul {}
               (for [collection-name collection-names]
                 (d/li {:key collection-name}
                       (Collection-Button collection-name handlers))))))
