package com.bellman.pm.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by Potencio on 11/21/2016.
 * <p>
 * Copyright 2016, Potencio
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

public class PopularMovieUtil {
    public static String buildPosterUrl(String posterPath)
    {
        return "http://image.tmdb.org/t/p/w185/"+posterPath;
    }

    public static String formatReleaseDate(String releaseDate) {

        SimpleDateFormat originalFormat = new SimpleDateFormat("yyyy-mm-dd", Locale.getDefault());
        Date date = null;
        try {
            date = originalFormat.parse(releaseDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        SimpleDateFormat newFormat = new SimpleDateFormat("MMMM dd, yyyy", Locale.getDefault());
        return newFormat.format(date);
    }
}
