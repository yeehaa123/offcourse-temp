(ns offcourse.helpers.css
  (:require [clojure.string :as string]))

(defn state-classes [base-class states]
  (for [state states] (str base-class"-is-"(name state))))

(defn classes [base-class & states]
  (let [classes (state-classes base-class states)]
    (string/join " " (conj classes base-class))))

(defn type-class [base-class type]
  (str base-class "-" (name type)))

(defn type-classes [base-class type]
  (string/join " " (conj [base-class] (type-class base-class type))))
