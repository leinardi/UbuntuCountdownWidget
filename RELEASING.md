# Releasing

1. Update the release date inside the Utils.kt file
2. `git commit -am "Set release date for YY.MM RELEASE_NAME"` (where YY.MM RELEASE_NAME is the Ubuntu release name)
3. `git tag -s vYY.MM && git push --tags`
4. `./gradlew clean assembleRelease`
5. Test the release APK
6. Crate a fastlane changelog file using the `BuildConfig.VERSION_CODE` as file name with `.txt` as extension.
7. `git commit -am "Updated fastlane changelog"`
8. `git push`
9. Create a new release on Github
    1. Tag version `vYY.MM`
    2. Release title `vYY.MM`
    3. Paste the content from from the fastlane changelog from 7. as the description
    4. Upload the sample-release.apk
10. Create a PR from [master](../../tree/master) to [release](../../tree/release)
11. Visit [Google Play Console](https://play.google.com/apps/publish/) and upload and publish the new APK
