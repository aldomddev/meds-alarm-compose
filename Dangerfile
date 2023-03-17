# Only focus on changes for the current DIFF from the active PR
github.dismiss_out_of_range_messages

# Is it working?
message "Welcome, Danger." if github.pr_author == "aldomddev"

# Identify if a PR is not yet meant to be merged, by commenting with a warning
# has_wip_label = github.pr_labels.any? { |label| label.include? "Engineers at work" }
# has_wip_title = github.pr_title.include? "[WIP]"
# if has_wip_label || has_wip_title
#   warn("PR is marked as Work in Progress")
# end

# Ensure the PR is not marked as DO NOT MERGE
# fail("PR specifies label DO NOT MERGE") if github.pr_labels.any? { |label| label.include? "DO NOT MERGE" }

# Warn when there is a big PR
# warn("Big PR") if git.lines_of_code > 5000

# To support multi module projects, identify defined modules via the settings.gradle
# Open the file `settings.gradle`
# File.open("settings.gradle", "r") do |file_handle|
#   # Read through every single file
#   file_handle.each_line do |setting|
#     # Only check lines which include `include`
#     if setting.include? "include"
#         # read in the module name
#         # Note this goes by the assumption that name equals path
#         gradleModule = setting[10, setting.length-12]
#
#         # Process check-stlye results
#         checkstyleFile = String.new(gradleModule + "/build/reports/checkstyle/checkstyle.xml")
#         if File.file?(checkstyleFile)
#             checkstyle_format.base_path = Dir.pwd
#             checkstyle_format.report(checkstyleFile, inline_mode: true)
#         end
#
#         # Process Android-Lint results
#         androidLintFile = String.new(gradleModule + "/build/reports/lint-results.xml")
#         androidLintDebugFile = String.new(gradleModule + "/build/reports/lint-results-debug.xml")
#         if File.file?(androidLintFile) || File.file?(androidLintDebugFile)
#             android_lint.skip_gradle_task = true # do this if lint was already run in a previous build step
#             android_lint.severity = "Warning"
#             if File.file?(androidLintFile)
#                 android_lint.report_file = androidLintFile
#             else
#                 android_lint.report_file = androidLintDebugFile
#             end
#             android_lint.filtering = true
#             android_lint.lint(inline_mode: true)
#         end
#
#         # Process Detekt results
#         detektFile = String.new(gradleModule + "/build/reports/detekt.xml")
#         if File.file?(detektFile)
#             kotlin_detekt.report_file = detektFile
#             kotlin_detekt.skip_gradle_task = true
#             kotlin_detekt.severity = "warning"
#             kotlin_detekt.filtering = true
#             kotlin_detekt.detekt(inline_mode: true)
#         end
#     end
#   end
# end

