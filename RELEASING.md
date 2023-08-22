# Releasing

1. Update the release date inside the Utils.kt file
2. Update the `changelog.xml`
3. `git commit -am "Set release date for YY.MM RELEASE_NAME"` (where YY.MM RELEASE_NAME is the Ubuntu release name)
4. `git tag -s vYY.MM`
5. `./gradlew clean assembleDebug`
6. `git ci --amend -av --no-edit`
7. `git tag -s -f vYY.MM && git push --tags`
8. `./gradlew clean assembleRelease bundleRelease`
9. Test the release APK
10. Crate a fastlane changelog file using the `BuildConfig.VERSION_CODE` as file name with `.txt` as extension.
11. `git commit -am "Updated fastlane changelog"`
12. `git push`
13. Create a new release on Github
    1. Tag version `vYY.MM`
    2. Release title `vYY.MM`
    3. Paste the content from from the fastlane changelog from 7. as the description
    4. Upload the sample-release.apk
14. Create a PR from [master](../../tree/master) to [release](../../tree/release)
15. Visit [Google Play Console](https://play.google.com/apps/publish/) and upload and publish the new APK
