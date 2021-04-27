(ns badams.repository)

(declare unsorted)

(defprotocol DetailsRepository
  (-put! [this line])
  (-values [this]))

(defn put! [repo details]
  (-put! repo details))

(defn values [repo & [opt]]
  (sort (get opt :order unsorted)
        (-values repo)))

(def unsorted (constantly 0))

(defn by-last-name-desc
  [lhs rhs]
  (compare (:LastName rhs)
           (:LastName lhs)))

(defn by-birth-date-asc
  [lhs rhs]
  (compare (:DateOfBirth lhs)
           (:DateOfBirth rhs)))

(defn by-email-desc-last-name-asc
  [lhs rhs]
  (compare [(:Email rhs) (:LastName lhs)]
           [(:Email lhs) (:LastName rhs)]))

(extend-type clojure.lang.IAtom2
  DetailsRepository
  (-values [this] @this)
  (-put! [this details]
    (swap! this conj details)))

(defn atomic-repo
  ([] (atomic-repo []))
  ([init] (atom init)))
