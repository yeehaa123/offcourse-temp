(ns offcourse.helpers.actions)

(defn map-object [f m]
  (into {} (for [[k v] m] [k (f v)])))

(defn bind-handlers [handlers id]
  (map-object #(partial %1 id) handlers))
