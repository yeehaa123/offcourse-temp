(ns offcourse.appstate.utils)

(defn get-course [{:keys [level]} {:keys [courses]}]
  (get courses (:course-id level)))

(defn get-checkpoint [checkpoint-id course]
  (get-in course [:checkpoints checkpoint-id]))

(defn get-collection [{:keys [level]} {:keys [collections courses]}]
  (let [collection-name (:collection-name level)]
    (->> collections
         collection-name
         (map (fn [id] [id (get courses id)]))
         (into {}))))
