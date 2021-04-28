(ns badams.core-test
  (:require [badams.core :as sut]
            [clojure.test :refer :all]))

(def date "12/12/1979")

(def details {:LastName      "LN"
              :FirstName     "FN"
              :Email         "EM"
              :FavoriteColor "FC"
              :DateOfBirth   (sut/date date)})

(def csv-line      "LN,FN,EM,FC,12/12/1979")
(def space-sv-line "LN FN EM FC 12/12/1979")
(def pipe-sv-line  "LN|FN|EM|FC|12/12/1979")

(def expected-details {:LastName      "LN"
                       :FirstName     "FN"
                       :Email         "EM"
                       :FavoriteColor "FC"
                       :DateOfBirth   date})

(deftest parse-test
  (testing "degenerate parsing case"
    (and (is (= nil (sut/parse nil)))
         (is (= nil (sut/parse "")))))

  (testing "parsing with different delimiters"
    (and
      (is (= details (sut/parse csv-line)))
      (is (= details (sut/parse space-sv-line)))
      (is (= details (sut/parse pipe-sv-line)))))

  (testing "parse failure"
    (and
      (is (= nil (sut/parse "1,2,3,4,bad-date")))
      (is (= nil (sut/parse "1"))))))

(deftest prepare-details-test
  (is (= expected-details (sut/format-details details))))
