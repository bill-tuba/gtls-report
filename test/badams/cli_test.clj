(ns badams.cli-test
  (:require [badams.cli :as sut]
            [badams.core :as core]
            [badams.repository :as repo]
            [clojure.string :as str]
            [clojure.test :refer :all]))

(deftest show-report!-test
  (let [repo (repo/atomic-repo [{:line 1 :DateOfBirth (core/date "1/2/1970")}
                                {:line 2 :DateOfBirth (core/date "1/3/1980")}])]

    (is (=
"|	TEST:
|
| line | DateOfBirth |
|------+-------------|
|    1 |    1/2/1970 |
|    2 |    1/3/1980 |"

         (str/trim (with-out-str (sut/show-report! repo [repo/unsorted "TEST"])))))))

(deftest create-repo!-test
  (is (= [{:FirstName "FN"
           :LastName "LN"
           :Email "EM"
           :FavoriteColor "FC"
           :DateOfBirth (core/date "1/1/1970")}]
         (repo/values (sut/create-repo! "./test/resources/cli_test.csv")))))

(deftest -main-test
  (let [results
        (str/trim
         (with-out-str
           (sut/-main "-f" "./test/resources/cli_int_test.csv")))]
    (is (=
"|\tBy email descending, last name ascending:
|
| LastName | FirstName | Email | FavoriteColor | DateOfBirth |
|----------+-----------+-------+---------------+-------------|
|       LN |        FN |   EM4 |            FC |    1/3/1990 |
|      LNB |        FN |   EM4 |            FC |    1/3/1990 |
|      LND |        FN |   EM4 |            FC |    1/3/1990 |
|      LNB |        FN |   EM3 |            FC |    1/1/1980 |
|      LNC |        FN |   EM2 |            FC |    1/5/2000 |
|      LN0 |        FN |   EM1 |            FC |    1/1/1960 |
                       

|\tBy birth date ascending:
|
| LastName | FirstName | Email | FavoriteColor | DateOfBirth |
|----------+-----------+-------+---------------+-------------|
|      LN0 |        FN |   EM1 |            FC |    1/1/1960 |
|      LNB |        FN |   EM3 |            FC |    1/1/1980 |
|      LNB |        FN |   EM4 |            FC |    1/3/1990 |
|       LN |        FN |   EM4 |            FC |    1/3/1990 |
|      LND |        FN |   EM4 |            FC |    1/3/1990 |
|      LNC |        FN |   EM2 |            FC |    1/5/2000 |
                       

|\tBy last name descending:
|
| LastName | FirstName | Email | FavoriteColor | DateOfBirth |
|----------+-----------+-------+---------------+-------------|
|      LND |        FN |   EM4 |            FC |    1/3/1990 |
|      LNC |        FN |   EM2 |            FC |    1/5/2000 |
|      LNB |        FN |   EM3 |            FC |    1/1/1980 |
|      LNB |        FN |   EM4 |            FC |    1/3/1990 |
|      LN0 |        FN |   EM1 |            FC |    1/1/1960 |
|       LN |        FN |   EM4 |            FC |    1/3/1990 |"
         results))))
