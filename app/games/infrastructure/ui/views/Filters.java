package games.infrastructure.ui.views;

import play.filters.csrf.CSRFFilter;
import play.filters.headers.SecurityHeadersFilter;
import play.filters.hosts.AllowedHostsFilter;
import play.http.DefaultHttpFilters;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class Filters extends DefaultHttpFilters {

    @Inject
    public Filters(CSRFFilter csrfFilter,
                   AllowedHostsFilter allowedHostsFilter,
                   SecurityHeadersFilter securityHeadersFilter) {
        super(csrfFilter, allowedHostsFilter, securityHeadersFilter);
    }
}
