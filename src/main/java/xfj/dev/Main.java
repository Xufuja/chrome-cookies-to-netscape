package xfj.dev;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;

public class Main {
    public static void main(String[] args) throws IOException {
        String inputPath = args.length == 1 ? args[0] : "chrome-cookies.txt";
        String content = Files.readString(Path.of(inputPath), StandardCharsets.UTF_8);
        String[] cookies = content.split("\n");

        String header = String.format("# Netscape HTTP Cookie File%1$s# http://curl.haxx.se/rfc/cookie_spec.html%1$s# This is a generated file!  Do not edit.%1$s%1$s", System.lineSeparator());
        Path outputPath = Path.of("cookies.txt");

        Files.writeString(outputPath,
                header,
                StandardCharsets.UTF_8,
                StandardOpenOption.CREATE,
                StandardOpenOption.TRUNCATE_EXISTING);
        System.out.println(header);

        for (String cookie : cookies) {
            String[] splitCookie = cookie.split("\t");
            String name = splitCookie[0];
            String value = splitCookie[1];
            String domain = splitCookie[2];
            String path = splitCookie[3];
            String expiration = splitCookie[4];
            //String size = splitCookie[5];
            String httpOnly = splitCookie[6];
            String includeSubdomain = "TRUE";

            if (name == null) {
                continue;
            }
            if (domain.charAt(0) != '.') {
                includeSubdomain = "FALSE";
            }
            httpOnly = httpOnly.equals("✓") ? "TRUE" : "FALSE";
            if (expiration.equals("Session")) {
                expiration = "0";
                /*
                Deal with this later
                expiration = new Date(Date.now() + 86400 * 1000);
                expiration = Math.trunc(new Date(expiration).getTime() / 1000);
                 */
            }
            String data = String.join("\t", domain, includeSubdomain, path, httpOnly, expiration, name, value) + System.lineSeparator();
            Files.writeString(outputPath,
                    data,
                    StandardCharsets.UTF_8,
                    StandardOpenOption.APPEND);
            System.out.println(data);
        }
    }
}