(ns offcourse.models.checkpoint)

(defrecord Checkpoint [id task url completed])

(defn temp-checkpoint []
  (map->Checkpoint {:id :new
                    :task "Do Something Different"
                    :url "bla.com"
                    :completed false}))

(defn new-checkpoint [checkpoint checkpoint-id]
    (map->Checkpoint (assoc checkpoint :id checkpoint-id)))

(defn toggle-done [checkpoint]
  (update-in checkpoint [:completed] not))

(defn add-resource [checkpoint resource]
  (assoc checkpoint :url (:url resource)
         :resource resource))
