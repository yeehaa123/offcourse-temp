(ns offcourse.api.fake-data
  (:require [offcourse.models.checkpoint :as cp]
            [offcourse.models.course :as co]))

(def urls ["facebook.com"
           "google.com"
           "yahoo.com"])

(def raw-courses [{:goal "Become a Frontend Ninja"
                   :checkpoints [{:task "Install React"
                                  :completed true}
                                 {:task "Build a Component"
                                  :completed false}
                                 {:task "Create an App"
                                  :completed false}]}
                  {:goal "Improve your Backend Chops"
                   :checkpoints [{:task "Install Node"
                                  :completed true}
                                 {:task "Set up a Route"
                                  :completed false}
                                 {:task "Add some Middleware"
                                  :completed false}
                                 {:task "Build an API"
                                  :completed false}]}
                  {:goal "Get More Street Cred"
                   :checkpoints [{:task "Talk Dirty with Reika"
                                  :completed false}
                                 {:task "Pair with Greg"
                                  :completed false}
                                 {:task "Scheme with Charlotte"
                                  :completed false}
                                 {:task "Brawl with Yeehaa"
                                  :completed false}]}
                  {:goal "Make DevOps Your Thing"
                   :checkpoints [{:task "Tame the Command Line"
                                  :completed false}
                                 {:task "Just Git It"
                                  :completed false}
                                 {:task "Try a PAAS"
                                  :completed false}
                                 {:task "Make Containers"
                                  :completed false}
                                 {:task "Do it All"
                                  :completed false}]}])


(defn index-checkpoint [checkpoint index]
  [index (cp/new-checkpoint (assoc checkpoint :url (rand-nth urls)) index)])

(defn index-checkpoints [checkpoints]
  (->> checkpoints
       (map-indexed #(index-checkpoint %2 (+ %1 100)))
       (into {})))

(defn index-course [course index]
  (let [checkpoints (index-checkpoints (:checkpoints course))]
    [index (assoc (co/new-course course index) :checkpoints checkpoints)]))

(defn indexed-courses [courses]
  (->> courses
       (map-indexed #(index-course %2 %1))
       (into {})))

(def courses
  (->> raw-courses
       indexed-courses))

(def resources
  (->> urls
       (map-indexed (fn [index url]
                      (if (= url "google.com")
                        [url {:url "offcourse.io"
                              :title (str "fake-resource " index)}]
                        [url {:url url
                              :title (str "fake-resource " index)}])))
       (into {})))
