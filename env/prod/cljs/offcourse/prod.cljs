(ns offcourse.prod
  (:require [offcourse.core :as core]))

;;ignore println statements in prod
;;(set! *print-fn* (fn [& _]))

(enable-console-print!)
(core/init!)
