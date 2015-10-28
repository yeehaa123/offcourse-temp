(ns offcourse.views.containers.topbar
  (:require [offcourse.views.components.breadcrumbs :refer [Breadcrumbs]]
            [offcourse.helpers.css :as css]
            [quiescent.dom :as d]))

(defn Topbar [topbar-data handlers]
  (d/section {:className (css/classes "topbar")}
             (Breadcrumbs topbar-data handlers)
             (d/div {:className "btn btn-authenticate"} "Authenticate")))

