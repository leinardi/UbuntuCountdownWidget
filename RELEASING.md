# Releasing

1. Update the release date inside the Utils.kt file
2. Update the `README.md` with the new version.
3. `git commit -am "Set release date for YY.MM RELEASE_NAME"` (where YY.MM RELEASE_NAME is the Ubuntu release name)
4. `git tag -s vYY.MM && git push --tags`
5. `./gradlew clean assembleRelease`
6. Test the release APK
7. Crate a fastlane changelog file using the `BuildConfig.VERSION_CODE` as file name with `.txt` as extension.
8. `git commit -am "Updated fastlane changelog"`
9. `git push`
10. Create a new release on Github
    1. Tag version `X.Y.Z`
    2. Release title `X.Y.Z`
    3. Paste the content from from the fastlane changelog from 7. as the description
    4. Upload the sample-release.apk
11. Create a PR from [master](../../tree/master) to [release](../../tree/release)
12. Visit [Google Play Console](https://play.google.com/apps/publish/) and upload and publish the new APK
