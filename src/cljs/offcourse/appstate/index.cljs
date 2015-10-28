(ns offcourse.appstate.index
  (:require-macros [cljs.core.async.macros :refer [go-loop]])
  (:require [offcourse.appstate.store :as store]
            [offcourse.appstate.service :as service]
            [cljs.core.async :refer [>! <!]]))


(defn listen-for-actions [{input :channel-in
                           output :channel-out}]
  (go-loop []
    (let [{type :type payload :payload} (<! input)]
      (case type
        :requested-resource         (do
                                      (>! output (service/get-data payload))
                                      (store/set-level payload))
        :requested-commit           (>! output (store/commit-data payload))
        :requested-level            (>! output (service/switch-route payload))
        :requested-done-toggle      (>! output (service/toggle-done payload))
        :requested-highlight-toggle (>! output (store/toggle-highlight payload))
        :requested-mode-toggle      (store/toggle-mode)
        :requested-mode-switch      (store/set-mode payload)
        :updated-data               (>! output (store/refresh payload))
        :checked-datastore          (>! output (store/refresh payload))
        :added-checkpoint           (>! output (service/return-to-course payload))
        :reloaded-code              (>! output (store/force-refresh))
        nil))
    (recur)))

(defn init [config]
  (listen-for-actions config))
