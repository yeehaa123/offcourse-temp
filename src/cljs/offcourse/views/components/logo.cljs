(ns offcourse.views.components.logo
  (:require [offcourse.helpers.css :as css]
            [quiescent.dom :as d]))

(defn Logo [{:keys [go-to-collection]}]
  (d/section {:className (css/classes "logo")}
             (d/button {:className (css/classes "textbar")
                        :onClick #(go-to-collection :featured)}
                       "Offcourse_")))
