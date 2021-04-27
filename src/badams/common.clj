(ns badams.common)

(defn options [args]
  (into {} (map vec (partition 2 args))))
