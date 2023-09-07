# Releasing

1. Update the `DEFAULT_RELEASE_DATE` inside the `com/leinardi/ubuntucountdownwidget/interactor/GetReleaseDateInteractor.kt` file
2. Run the [Update release date](https://github.com/leinardi/UbuntuCountdownWidget/actions/workflows/ubuntu-release-date.yml) Action
3. `git commit -am "Set release date for YY.MM"` (where YY.MM RELEASE_NAME is the Ubuntu release name)
4. `./gradlew clean assembleDebug`
5. `git ci --amend -av --no-edit`
6. `./gradlew clean assembleRelease bundleRelease`
7. Test the release APK
8. Crate a fastlane changelog file using the `BuildConfig.VERSION_CODE` as file name with `.txt` as extension.
9. `git commit -am "Updated fastlane changelog"`
10. `git push`
11. Run the [Release](https://github.com/leinardi/UbuntuCountdownWidget/actions/workflows/release.yml) Action
12. Create a PR from [master](../../tree/master) to [release](../../tree/release)
13. Visit [Google Play Console](https://play.google.com/apps/publish/) and promote the release
