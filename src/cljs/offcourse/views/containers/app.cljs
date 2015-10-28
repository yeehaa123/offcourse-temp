(ns offcourse.views.containers.app
  (:require [offcourse.helpers.css :as css]
            [offcourse.views.containers.sidebar :refer [Sidebar]]
            [offcourse.views.containers.topbar :refer [Topbar]]
            [offcourse.views.containers.cards :refer [Cards]]
            [quiescent.dom :as d]))

(defn App [handlers {:keys [viewmodel mode]}]
  (d/section {:className (css/classes "app" mode "waypoints")}
             (d/div {:className "layout-sidebar"}
                    (Sidebar viewmodel handlers))
             (d/div {:className "layout-right"}
                    (d/div {:className "layout-topbar"}
                           (Topbar viewmodel handlers))
                    (d/div {:className "layout-main"}
                           (Cards viewmodel handlers)))))
