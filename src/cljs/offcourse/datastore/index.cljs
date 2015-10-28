(ns offcourse.datastore.index
  (:require-macros [cljs.core.async.macros :refer [go-loop]])
  (:require        [cljs.core.async :refer [>! <!]]
                   [offcourse.datastore.store :as store]))

(defn listen-for-actions [{input  :channel-in
                                 output :channel-out}]
  (go-loop []
    (let [{type :type payload :payload} (<! input)]
      (case type
        :requested-data             (>! output (store/get-data payload))
        :requested-commit           (>! output (store/commit-data payload))
        :fetched-data               (>! output (store/update-datastore payload))
        :requested-toggle-done      (>! output (store/toggle-done payload))
        nil))
    (recur)))

(defn init [config]
  (listen-for-actions config))
